package policyboss.com.inspect.core.inspection;

/**
 * Created by Nilesh Birhade on 13-12-2017.
 */

public class APIResponse {

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
