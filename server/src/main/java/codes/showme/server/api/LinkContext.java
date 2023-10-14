package codes.showme.server.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkContext {
    @JsonProperty("href")
    private String href;
    @JsonProperty("text")
    private String text;

    public LinkContext(String href, String text) {
        this.href = href;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkContext that = (LinkContext) o;
        return Objects.equal(href, that.href) && Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(href, text);
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
