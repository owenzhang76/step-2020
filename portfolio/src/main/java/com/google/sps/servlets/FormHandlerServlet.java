// package com.google.sps.servlets;

// import com.google.appengine.api.blobstore.BlobInfo;
// import com.google.appengine.api.blobstore.BlobInfoFactory;
// import com.google.appengine.api.blobstore.BlobKey;
// import com.google.appengine.api.blobstore.BlobstoreService;
// import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
// import com.google.appengine.api.datastore.DatastoreService;
// import com.google.appengine.api.datastore.DatastoreServiceFactory;
// import com.google.appengine.api.datastore.Entity;
// import com.google.appengine.api.datastore.PreparedQuery;
// import com.google.appengine.api.datastore.Query;
// import com.google.appengine.api.datastore.Query.SortDirection;
// import com.google.appengine.api.datastore.FetchOptions;
// import com.google.appengine.api.datastore.*;
// import com.google.appengine.api.images.ImagesService;
// import com.google.appengine.api.images.ImagesServiceFactory;
// import com.google.appengine.api.images.ServingUrlOptions;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.net.MalformedURLException;
// import java.net.URL;
// import java.util.List;
// import java.util.Map;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// /**
//  * When the user submits the form, Blobstore processes the file upload and then forwards the request
//  * to this servlet. This servlet can then process the request using the file URL we get from
//  * Blobstore.
//  */
// @WebServlet("/form-handler")
// public class FormHandlerServlet extends HttpServlet {

//   @Override
//   public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//     System.out.println("Inside doPost of /form-handler");
//     String comment = request.getParameter("comment-input");
//     String senderName = request.getParameter("chatname-input");
//     long timestamp = System.currentTimeMillis();
//     String imageUrl = getUploadedFileUrl(request, "file-input");

//     System.out.println("imageUrl is " + imageUrl);

//     Entity messageEntity = new Entity("Message");
//     messageEntity.setProperty("body", comment);
//     messageEntity.setProperty("timestamp", timestamp);
//     messageEntity.setProperty("senderName", senderName);
//     messageEntity.setProperty("imageUrl", imageUrl);

//     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//     datastore.put(messageEntity);

//     response.sendRedirect("/forum.html");
   
//   }

//   /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
//   private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
//     System.out.println("getUplaodedFileUrl ran");
//     BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//     Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
//     List<BlobKey> blobKeys = blobs.get("file-input");
//     System.out.println("blob keys");
//     System.out.println(blobKeys);

//     // User submitted form without selecting a file, so we can't get a URL. (dev server)
//     if (blobKeys == null || blobKeys.isEmpty()) {
//       return null;
//     }

//     // Our form only contains a single file input, so get the first index.
//     BlobKey blobKey = blobKeys.get(0);
//     System.out.println(blobKey);

//     // User submitted form without selecting a file, so we can't get a URL. (live server)
//     BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
//     if (blobInfo.getSize() == 0) {
//       blobstoreService.delete(blobKey);
//       return null;
//     }

//     // We could check the validity of the file here, e.g. to make sure it's an image file
//     // https://stackoverflow.com/q/10779564/873165

//     // Use ImagesService to get a URL that points to the uploaded file.
//     ImagesService imagesService = ImagesServiceFactory.getImagesService();
//     ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

//     // To support running in Google Cloud Shell with AppEngine's devserver, we must use the relative
//     // path to the image, rather than the path returned by imagesService which contains a host.
//     try {
//       URL url = new URL(imagesService.getServingUrl(options));
//       System.out.println(url.getPath());
//       return url.getPath();
//     } catch (MalformedURLException e) {
//       return imagesService.getServingUrl(options);
//     }
//   }
// }
