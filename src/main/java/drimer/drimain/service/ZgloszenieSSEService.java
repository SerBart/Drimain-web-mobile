package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieEventDTO;
import drimer.drimain.model.enums.ZgloszenieEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Serwis zarządzający subskrypcjami SSE dla eventów zgłoszeń.
 * Obsługuje filtrowanie, heartbeat i limit połączeń.
 */
@Service
public class ZgloszenieSSEService {

    private static final Logger log = LoggerFactory.getLogger(ZgloszenieSSEService.class);
    
    private final int maxClients;
    private final int timeoutMinutes; 
    private final int heartbeatSeconds;

    // Mapa emitters z ich filtrami
    private final Map<String, EmitterSubscription> subscriptions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private boolean heartbeatStarted = false;

    public ZgloszenieSSEService(@Value("${app.sse.max-clients:100}") int maxClients,
                               @Value("${app.sse.timeout-minutes:30}") int timeoutMinutes,
                               @Value("${app.sse.heartbeat-seconds:15}") int heartbeatSeconds) {
        this.maxClients = maxClients;
        this.timeoutMinutes = timeoutMinutes;
        this.heartbeatSeconds = heartbeatSeconds;
    }

    private void ensureHeartbeatStarted() {
        if (!heartbeatStarted && heartbeatSeconds > 0) {
            scheduler.scheduleAtFixedRate(this::sendHeartbeat, heartbeatSeconds, heartbeatSeconds, TimeUnit.SECONDS);
            heartbeatStarted = true;
        }
    }

    /**
     * Tworzy nową subskrypcję SSE z filtrami.
     */
    public SseEmitter subscribe(Set<ZgloszenieEventType> eventTypes, Long dzialId, Long autorId, boolean fullData) {
        if (subscriptions.size() >= maxClients) {
            log.warn("Max SSE clients limit reached: {}", maxClients);
            return null; // Zwróć null - kontroler wyśle 503
        }

        ensureHeartbeatStarted(); // Uruchom heartbeat gdy pierwszy klient się podłączy

        String subscriptionId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(timeoutMinutes));
        
        EmitterSubscription subscription = new EmitterSubscription(emitter, eventTypes, dzialId, autorId, fullData);
        subscriptions.put(subscriptionId, subscription);

        emitter.onCompletion(() -> subscriptions.remove(subscriptionId));
        emitter.onTimeout(() -> subscriptions.remove(subscriptionId));
        emitter.onError(throwable -> subscriptions.remove(subscriptionId));

        log.info("New SSE subscription created: {} (total: {})", subscriptionId, subscriptions.size());
        return emitter;
    }

    /**
     * Rozgłasza event do wszystkich pasujących subskrypcji.
     */
    public void broadcast(ZgloszenieEventDTO eventDTO) {
        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, EmitterSubscription> entry : subscriptions.entrySet()) {
            String id = entry.getKey();
            EmitterSubscription sub = entry.getValue();
            
            if (shouldSendToSubscription(sub, eventDTO)) {
                try {
                    // Przygotuj dane do wysłania (pełne lub minimalne)
                    ZgloszenieEventDTO toSend = prepareEventForSubscription(sub, eventDTO);
                    sub.emitter.send(SseEmitter.event()
                            .name("zgloszenie-event")
                            .data(toSend));
                } catch (IOException e) {
                    log.debug("Failed to send event to subscription {}, removing", id);
                    toRemove.add(id);
                }
            }
        }

        // Usuń niedziałające subskrypcje
        toRemove.forEach(subscriptions::remove);
    }

    private boolean shouldSendToSubscription(EmitterSubscription sub, ZgloszenieEventDTO event) {
        // Sprawdź typ eventu
        if (sub.eventTypes != null && !sub.eventTypes.isEmpty() && !sub.eventTypes.contains(event.getType())) {
            return false;
        }

        // Sprawdź filtrowanie po dziale (wymagane pełne dane)
        if (sub.dzialId != null && event.getData() != null) {
            if (!Objects.equals(sub.dzialId, event.getData().getDzialId())) {
                return false;
            }
        }

        // Sprawdź filtrowanie po autorze (wymagane pełne dane)
        if (sub.autorId != null && event.getData() != null) {
            if (!Objects.equals(sub.autorId, event.getData().getAutorId())) {
                return false;
            }
        }

        return true;
    }

    private ZgloszenieEventDTO prepareEventForSubscription(EmitterSubscription sub, ZgloszenieEventDTO original) {
        if (!sub.fullData) {
            // Wyślij minimalną wersję - usuń dane
            return new ZgloszenieEventDTO(
                original.getType(),
                original.getZgloszenieId(),
                original.getTimestamp(),
                null, // brak pełnych danych
                original.getChangedFields(),
                original.getAttachmentId()
            );
        }
        return original; // wyślij pełną wersję
    }

    private void sendHeartbeat() {
        List<String> toRemove = new ArrayList<>();
        
        Map<String, Object> heartbeatData = Map.of("timestamp", LocalDateTime.now());

        for (Map.Entry<String, EmitterSubscription> entry : subscriptions.entrySet()) {
            try {
                entry.getValue().emitter.send(SseEmitter.event()
                        .name("heartbeat")
                        .data(heartbeatData));
            } catch (IOException e) {
                log.debug("Heartbeat failed for subscription {}, removing", entry.getKey());
                toRemove.add(entry.getKey());
            }
        }

        toRemove.forEach(subscriptions::remove);
    }

    public int getActiveSubscriptions() {
        return subscriptions.size();
    }

    /**
     * Wewnętrzna klasa reprezentująca subskrypcję.
     */
    private static class EmitterSubscription {
        final SseEmitter emitter;
        final Set<ZgloszenieEventType> eventTypes;
        final Long dzialId;
        final Long autorId;
        final boolean fullData;

        EmitterSubscription(SseEmitter emitter, Set<ZgloszenieEventType> eventTypes, 
                          Long dzialId, Long autorId, boolean fullData) {
            this.emitter = emitter;
            this.eventTypes = eventTypes;
            this.dzialId = dzialId;
            this.autorId = autorId;
            this.fullData = fullData;
        }
    }
}