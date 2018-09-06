package policyboss.com.inspect.core.inspection;


/**
 * Created by Nilesh Birhade on 17-10-2016.
 */

public interface IResponseSubcribe {

    void OnSuccess(APIResponse response, String message);

    void OnFailure(Throwable t);
}
