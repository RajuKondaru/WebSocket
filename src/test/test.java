package test;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String num="9676769679";
		System.out.println(num.length());
		if(num.matches(".*\\d+.*")){
			long l = Long.parseLong(num);
			
			System.out.println(l);
		}
		
	}

}
