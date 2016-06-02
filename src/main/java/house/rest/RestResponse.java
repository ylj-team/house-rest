package house.rest;

public class RestResponse {
	
	public int code;
	public String msg;
	public Object body;
	
	public RestResponse(int code,String msg,Object body){
		this.code=code;
		this.msg=msg;
		this.body=body;
	}
	
	public static RestResponse createSuccessResponse(){
		return new RestResponse(0,"success",null);
	}
	public static RestResponse createErrorResponse(){
		return new RestResponse(-1,"error",null);
	}
}
