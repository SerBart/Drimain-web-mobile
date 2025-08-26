package drimer.drimain.api.dto;

import java.util.List;

/**
 * Page wrapper for Zgloszenie pagination as specified in the requirements.
 * Compatible with Spring Data Page interface.
 */
public class ZgloszeniePage {
    
    private List<ZgloszenieDTO> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;

    public ZgloszeniePage() {}

    public ZgloszeniePage(List<ZgloszenieDTO> content, long totalElements, int totalPages, int size, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

    public List<ZgloszenieDTO> getContent() { return content; }
    public void setContent(List<ZgloszenieDTO> content) { this.content = content; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
}