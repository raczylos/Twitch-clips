package com.example.twitch.streamer;

public class streamerException {
    public static class StreamerAlreadyExistsException extends RuntimeException {
        public StreamerAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidStreamerLoginException extends RuntimeException {
        public InvalidStreamerLoginException(String message) {
            super(message);
        }
    }
}
