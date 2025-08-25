-- Opcjonalny skrypt Flyway (jeżeli używasz Flyway). Zmień numer wersji jeśli już masz V1.
-- Normalizacja starych wartości tekstowych na nowy format ENUM / kanoniczny String.
-- Dostosuj dialekt do własnej bazy.

-- PRZYKŁAD dla baz wspierających UPDATE:
UPDATE harmonogramy SET status = 'BRAK_CZESCI' WHERE LOWER(status) IN ('brak czesc','brak część','brak części');
UPDATE harmonogramy SET status = 'OCZEKIWANIE_NA_CZESC' WHERE LOWER(status) IN ('oczekiwanie na czesc','oczekiwanie na część','oczekiwanie na części');

-- Jeżeli istnieją rekordy bez statusu (NULL):
UPDATE harmonogramy SET status = 'PLANOWANE' WHERE status IS NULL;