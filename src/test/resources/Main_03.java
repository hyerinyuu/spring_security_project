import java.util.Arrays;

public class Main_03 {

	public static void main(String[] args) {

		int[] num = new int[3];
		num[0] = 100;
		num[1] = 100;
		num[2] = 100;
		
		// num배열을 List형 객체로 변경
		// Arrays.asList(배열) : 실제 완전한 List로 변경이 아니다. 무늬만 List
		// 이 method로 변경된 List는 추가, 삭제, 내용변경이 불가
		// 완전한 List형으로 변경하기 위해서는
		// ArrayList, LinkedList등의 생성자를 사용해서 List로 변경해주는 절차가 필요하다.
		List<Integer> numList = new ArrayList<Integer>(Arrays.asList(num));
		
	}

}
