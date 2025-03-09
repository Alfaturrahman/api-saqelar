package com.example.tester.models;

public class ApiResponse {
    private boolean success;
    private Object data;  // Bisa berisi MessageData atau Data (berisi token)

    // Konstruktor untuk sukses dengan objek MessageData (pesan sukses/error)
    public ApiResponse(boolean success, MessageData data) {
        this.success = success;
        this.data = data;
    }

    // Konstruktor untuk sukses dengan objek Data (berisi token)
    public ApiResponse(boolean success, Data data) {
        this.success = success;
        this.data = data;
    }

    // Konstruktor untuk sukses dengan data berupa string (misalnya pesan error atau informasi lainnya)
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.data = new MessageData(message);  // Mengonversi pesan ke dalam MessageData
    }

    // Konstruktor jika tidak ada data yang perlu dikirim (misalnya pada registrasi)
    public ApiResponse(boolean success) {
        this.success = success;
        this.data = null;  // Tidak ada data
    }

    // Getter dan Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // Static nested class MessageData untuk menyimpan pesan
    public static class MessageData {
        private String message;

        // Konstruktor untuk menyimpan pesan
        public MessageData(String message) {
            this.message = message;
        }

        // Getter dan Setter untuk pesan
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Static nested class Data untuk menyimpan token
    public static class Data {
        private String token;
        private String message;

        public Data(String token, String message) {
            this.token = token;
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
