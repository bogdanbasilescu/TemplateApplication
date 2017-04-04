package ro.basilescu.bogdan.templateapplication.rest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface FileClient {

    /*
     * Retrofit file upload requests
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhoto(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part photo);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhotos(
            @Part MultipartBody.Part profile,
            @Part MultipartBody.Part panorama);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadAlbum(
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhotoWithMultipartBodyPart(
            @Part("description") RequestBody description,
            @Part("location") RequestBody location,
            @Part("photographer") RequestBody photographer,
            @Part("year") RequestBody year,
            @Part MultipartBody.Part photo);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhotoWithPartMap(
            @PartMap Map<String, RequestBody> data,
            @Part MultipartBody.Part photo);

    /*
     * Retrofit file download requests
     */

    // option 1: a resource relative to your base URL
    @GET("/resource/example.zip")
    Call<ResponseBody> downloadFileWithFixedUrl();

    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
