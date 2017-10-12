package com.morpho.server;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;

/**
 * Created by irvin on 10/9/17.
 * This is a simple class that will handle response, just to avoid
 * unnecessary repetition of code, cause you know, this is JAVA,
 * we use objects and stuff like that.
 */
public class ResponseCreator {

    //Se there is this class that java has for HTTP response, it's like very
    //cool
    Response responseBuilder;

    public ResponseCreator(){

    }

    /**
     * Will create a response to send string/text/html data.
     * @param stringResponse is the data to sent out as a string (HTML for example)
     * @return built response with 200 status.
     */
    public Response respondSuccessfullyString(String stringResponse){
        return responseBuilder.ok(stringResponse, MediaType.TEXT_HTML_TYPE).build();
    }

    /**
     * Will create a response to send json object data.
     * @param jsonResponse is the string representing the json object.
     * @return built response with 200 status.
     */
    public Response respondSuccessfullyJSON(String jsonResponse){
        return responseBuilder.ok(jsonResponse, MediaType.APPLICATION_JSON_TYPE).build();
    }


    /**
     * Will create a response with whatever status. Recommended when handling
     * different types of errors. Though the other method with the same name and no status
     * will generate a response with the 412 code.
     * @param message provides information about the response.
     * @param status specifies the status of the response.
     * @return response.
     */
    public Response respondGenerally(String message, int status){
        return responseBuilder.status(status).entity(message).build();
    }

    /**
     * Will create a response with a 412 status. Could be used to handle errors generally.
     * @param errorMessage privides information about the error to the client.
     * @return response.
     */
    public Response respondWithFailure(String errorMessage){

        //412 is for preconditions fails, I guess you could use this with any exception for now.
        return respondGenerally(errorMessage,412);
    }



}
