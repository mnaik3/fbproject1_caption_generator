package com.example.caption_generator_app;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.Entity.AnnotationEntity;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servlet implementation class CloudVision
 */
@WebServlet("/CloudVision")
public class CloudVision extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    
    public CloudVision() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String imageurl = request.getParameter("hiddenField");
        String FbPhotoId = request.getParameter("Fb_image_id");
        
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");

        
        request.setAttribute("FbPhotoId", FbPhotoId);
        request.setAttribute("imageurl", imageurl);
        request.setAttribute("blobKeys", blobKeys);
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/upload");
        
        dispatcher.forward(request, response);
		
		
	}
	
	 private static byte[] downloadFile(URL url) throws Exception {
	        try (InputStream in = url.openStream()) {
	            byte[] bytes = IOUtils.toByteArray(in);
	            return bytes;
	        }
	    }
	 
	 public List<AnnotationEntity> getImageLabels(String imageUrl) {
		 
		 try {
			 
			 ImageAnnotatorClient vision = ImageAnnotatorClient.create();
			 
	            byte[] data = downloadFile(new URL(imageUrl));
	            ByteString byteString = ByteString.copyFrom(data);
	            
	         // Builds the image annotation request
			      List<AnnotateImageRequest> requests = new ArrayList<>();
			      Image img = Image.newBuilder().setContent(byteString).build();
			      Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
			      AnnotateImageRequest request =
			          AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			      requests.add(request);
			      
			   // Performs label detection on the image file
			      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			      List<AnnotateImageResponse> responses = response.getResponsesList();
			      			     
			      for (AnnotateImageResponse res : responses) {
				        if (res.hasError()) {
				          System.out.format("Error: %s%n", res.getError().getMessage());
				          return null;
				        }

				        List<AnnotationEntity> labelList=new ArrayList<AnnotationEntity>();
					    
				        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
				        	
				        	 AnnotationEntity lableEntity= new AnnotationEntity();
				        	 				        							 
				        	 String label = annotation.getDescription();
				        	 float score = annotation.getScore()*100;
				        	 
				        	 lableEntity.setLabel(label);
				        	 lableEntity.setScore(String.valueOf(score));
				        	 
				        	 labelList.add( lableEntity);
				        
				        }
				        
				        return labelList;
			      }
			      
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 
		 
		 return null;
		 
	 }

}
