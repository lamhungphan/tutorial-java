package module_4_exception._44_exception_handling;

public class ThrowAndThrows {
// throw : được dùng để tạo ra một ngoại lệ cụ thể và đẩy ra khỏi phương thức hiện tại
//1 - không để java tự ném
//2 - tự tạo đối tượng để ném
//3 - tự ném ngoại lệ

// throws: khai báo một phương thức có thể gây ra một hoặc nhiều ngoại lệ
	public static void main(String[] args) {
		String text = "abcd";
		doDaiString(text);

		String text2 = null;
		doDaiString(text2);
	}

	public static void doDaiString(String chuoi)
			throws NullPointerException, TenTrongKhongException {
		if (chuoi == null) {
			//tạo đối tượng
//			throw new NullPointerException("Chuỗi không được null");
			throw new TenTrongKhongException("Chuỗi không được null");
		}
		System.out.println("độ dài chuỗi: " + chuoi.length());
	}
}

//tự tạo ngoại lệ
class TenTrongKhongException extends RuntimeException{
	public TenTrongKhongException(String thongDiep) {
		super(thongDiep);
	}
}
