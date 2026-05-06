package module_2_core._52_immutable_design;

import java.util.List;
import java.util.Objects;

public class BuilderAndWith {

    // Khi class immutable có nhiều field -> dùng BUILDER (Effective Java Item 2).

    public static final class HttpRequest {
        private final String method;
        private final String url;
        private final List<String> headers;
        private final byte[] body;
        private final int timeoutMs;
        private final boolean followRedirect;

        private HttpRequest(Builder b) {
            this.method         = Objects.requireNonNull(b.method);
            this.url            = Objects.requireNonNull(b.url);
            this.headers        = List.copyOf(b.headers);
            this.body           = b.body == null ? null : b.body.clone();
            this.timeoutMs      = b.timeoutMs;
            this.followRedirect = b.followRedirect;
        }

        // "with*" pattern — trả NEW instance với 1 field thay đổi (immutable update)
        public HttpRequest withTimeout(int newTimeoutMs) {
            return toBuilder().timeoutMs(newTimeoutMs).build();
        }
        public HttpRequest withFollowRedirect(boolean v) {
            return toBuilder().followRedirect(v).build();
        }
        public Builder toBuilder() {
            return new Builder(method, url)
                    .headers(headers)
                    .body(body)
                    .timeoutMs(timeoutMs)
                    .followRedirect(followRedirect);
        }

        public String method() { return method; }
        public String url()    { return url; }
        public int timeoutMs() { return timeoutMs; }

        @Override public String toString() {
            return method + " " + url + " (timeout=" + timeoutMs + ", follow=" + followRedirect + ")";
        }

        public static Builder get(String url) { return new Builder("GET", url); }
        public static Builder post(String url) { return new Builder("POST", url); }

        public static final class Builder {
            private final String method;
            private final String url;
            private List<String> headers = List.of();
            private byte[] body;
            private int timeoutMs = 30_000;
            private boolean followRedirect = true;

            public Builder(String method, String url) {
                this.method = method;
                this.url = url;
            }
            public Builder headers(List<String> h)        { this.headers = h; return this; }
            public Builder body(byte[] b)                  { this.body = b; return this; }
            public Builder timeoutMs(int t)                { this.timeoutMs = t; return this; }
            public Builder followRedirect(boolean v)       { this.followRedirect = v; return this; }
            public HttpRequest build()                     { return new HttpRequest(this); }
        }
    }

    public static void main(String[] args) {
        var req = HttpRequest.get("https://api.example.com/users")
                .headers(List.of("Accept: application/json"))
                .timeoutMs(5_000)
                .build();

        var slow = req.withTimeout(60_000);

        System.out.println(req);
        System.out.println(slow);
        System.out.println(req == slow);              // false — đã copy
        System.out.println(req.timeoutMs());          // 5000
        System.out.println(slow.timeoutMs());         // 60000
    }
}
