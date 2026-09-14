public class Singleton {

    private static Singleton instance;

    private Singleton() {
        // Constructor private: "Ngăn chặn khởi tạo từ bên ngoài"
    }

    public static Singleton getInstance() {
        if (instance == null) {
            // Nếu chưa có thì mới bắt đầu tạo
            instance = new Singleton();
        }
        return instance;
    }
}