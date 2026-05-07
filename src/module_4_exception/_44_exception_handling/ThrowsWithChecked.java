package module_4_exception._44_exception_handling;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ThrowsWithChecked {
// throws: khai báo một phương thức có thể gây ra một hoặc nhiều ngoại lệ
	//unchecked: ngoại lệ KHÔNG được kiểm tra tại thời điểm biên dịch
	//checked: ngoại lệ được kiểm tra tại thời điểm biên dịch
	//từ khoá throws thường được dùng với từ khoá checked
	//ducking exception : để hàm khác xử lí ngoại lệ thay cho mỗi lần gọi
	public static void main(String[] args) throws FileNotFoundException{
		String text = "abcd";
		doDaiString(text);
		
		docFile("demo.txt");
	}
	//Checked Exception :  bắt buộc phải xử lí ngoại lệ để build và chạy chương trình
	public static void docFile(String tenFile) throws FileNotFoundException {
		try {
			FileReader reader = new FileReader(tenFile);
			//thao tác với file
		} catch (FileNotFoundException e) {
			//
			e.printStackTrace();
		}
	}

	public static void doDaiString(String chuoi) throws NullPointerException, TenTrongKhongException {
		if (chuoi == null) {
			// tạo đối tượng
			throw new NullPointerException("Chuỗi không được null");
		}
		System.out.println("độ dài chuỗi: " + chuoi.length());
	}
}
