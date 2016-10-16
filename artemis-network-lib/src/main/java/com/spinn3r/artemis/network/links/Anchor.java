package com.spinn3r.artemis.network.links;

/**
 * An anchor as represented as HTML and we try to be as close to the spec as
 * possible.
 */
public class Anchor {

    private String absHref = null;

    private String href = null;

    private String ownText = null;

    private String text = null;

    private String rel = null;

    private String type = null;

    public String getAbsHref() {
        return absHref;
    }

    public String getHref() {
        return href;
    }

    public String getOwnText() {
        return ownText;
    }

    public String getText() {
        return text;
    }

    public String getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Anchor{" +
                 "absHref='" + absHref + '\'' +
                 ", href='" + href + '\'' +
                 ", ownText='" + ownText + '\'' +
                 ", text='" + text + '\'' +
                 ", rel='" + rel + '\'' +
                 ", type='" + type + '\'' +
                 '}';
    }

    public static class Builder {

        private Anchor anchor = new Anchor();

        public Builder setAbsHref(String absHref) {
            anchor.absHref = absHref;
            return this;
        }

        public Builder setHref(String href) {
            anchor.href = href;
            return this;
        }

        public Builder setOwnText(String ownText) {
            anchor.ownText = ownText;
            return this;
        }

        public Builder setText(String text) {
            anchor.text = text;
            return this;
        }

        public Builder setRel(String rel) {
            anchor.rel = rel;
            return this;
        }

        public Builder setType(String type) {
            anchor.type = type;
            return this;
        }

        public Anchor build() {
            return anchor;
        }

    }

}
