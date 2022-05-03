package com.example.caption_generator_app;

import com.example.Entity.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.*;
import com.example.Entity.AnnotationEntity;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet implementation class Upload
 */
@WebServlet(
		name = "Upload",
		urlPatterns = {"/upload"}
)
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	
    public Upload() {
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
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	
		
		String fileName=req.getParameter("fileName");

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {
        	String FbPhotoId=(String) req.getAttribute("FbPhotoId");
        	 String fbImageUrl=(String) req.getAttribute("imageurl");
        	//List<BlobKey> fbblobKey = (List<BlobKey>) req.getAttribute("blobkeys");
        	if(FbPhotoId == null || fbImageUrl.isEmpty()) {
        		res.sendRedirect("/");
        	}
        	else {
        		URL url=new URL(fbImageUrl);
        		
        		byte[] blobBytes = null;
				try {
					blobBytes = downloadFile(url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		//get labels and score
            	List<AnnotationEntity> labelList = getImageLabels(blobBytes);
            	// get image story from labels so implement method and return story
            	String story = getImageStory(labelList);
            	
            	addImageToStore(fbImageUrl,story,FbPhotoId,datastore);
    			getImageFromStore(req, res, datastore, FbPhotoId,labelList);
    	  
            	
        	}
        } else {
        	//get image URL
        	
        	String imageUrl = generateImageUrl(blobKeys);
        	byte[] blobBytes = getBlobBytes(blobKeys.get(0));
        	
        	
        	//get labels and score
        	List<AnnotationEntity> labelList = getImageLabels(blobBytes);
        	// get image story from labels so implement method and return story
        	String story = getImageStory(labelList);
        	
        	addImageToStore(imageUrl,story,fileName,datastore);
			getImageFromStore(req, res, datastore, fileName,labelList);
	  
        	
			/*
			 * Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
			 * KeyFactory keyFactory = datastore.newKeyFactory().setKind("UserImage"); Key
			 * key = keyFactory.newKey("myFile"); Entity userImage = Entity.newBuilder(key)
			 * .set("ImageStory", "This picture has a beautiful story") .set("ImageUrl",
			 * imageUrl) .set("ImageID", "myImageId") .build(); datastore.put(userImage);
			 */
        
        }
        
       
	}
	
	 private static byte[] downloadFile(URL url) throws Exception {
	        try (InputStream in = url.openStream()) {
	            byte[] bytes = IOUtils.toByteArray(in);
	            return bytes;
	        }
	    }
	
	private String generateImageUrl(List<BlobKey> blobKeys) {
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0));
    	String imageUrl = imagesService.getServingUrl(options);
    	return imageUrl;
	}
	
	
	private byte[] getBlobBytes(BlobKey blobKey) throws IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();

		int fetchSize = BlobstoreService.MAX_BLOB_FETCH_SIZE;
		long currentByteIndex = 0;
		boolean continueReading = true;
		while (continueReading) {
			// end index is inclusive, so we have to subtract 1 to get fetchSize bytes
			byte[] b = blobstoreService.fetchData(blobKey, currentByteIndex, currentByteIndex + fetchSize - 1);
			outputBytes.write(b);

			// if we read fewer bytes than we requested, then we reached the end
			if (b.length < fetchSize) {
				continueReading = false;
			}

			currentByteIndex += fetchSize;
		}

		return outputBytes.toByteArray();
	}
	
	private List<AnnotationEntity> getImageLabels(byte[] imgBytes) throws IOException {
		
		ImageAnnotatorClient vision = ImageAnnotatorClient.create();
		
		ByteString byteString = ByteString.copyFrom(imgBytes);
		
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
	      
			return null;
	}

	
	private void getImageFromStore(HttpServletRequest request, HttpServletResponse response, DatastoreService datastore, String imageId,List<AnnotationEntity> labelList) {

        Query query =
                new Query("UserImage")
                        .setFilter(new Query.FilterPredicate("ImageID", Query.FilterOperator.EQUAL, imageId));
        PreparedQuery pq = datastore.prepare(query);
        List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
        if(null != results) {
            results.forEach(userPhoto -> {
                String ImageStoryFromDS = (String) userPhoto.getProperty("ImageStory");
                String ImageUrl=userPhoto.getProperty("ImageUrl").toString();
                request.setAttribute("ImageUrl",ImageUrl);
                request.setAttribute("ImageStory", ImageStoryFromDS);
                request.setAttribute("labelList", labelList);
                RequestDispatcher dispatcher = getServletContext()
                        .getRequestDispatcher("/labels.jsp");
                try {
                    dispatcher.forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
	
	
	public static void addImageToStore(String imageUrl, String imageStory, String imageId, DatastoreService datastore) {
		Entity User_Images = new Entity("UserImage",imageId);
        User_Images.setProperty("ImageID",imageId );
        User_Images.setProperty("ImageUrl", imageUrl);
        User_Images.setProperty("ImageStory", imageStory);
        
        datastore.put(User_Images);
        
    }
	
	public String getImageStory(List<AnnotationEntity> labelList) {
		words word=new words();
		 String line = new String();
		  //List<String> ll= new ArrayList<String>();
	    
	    int j;
		for (int i=0;i<labelList.size();i++) {
			
			j=i+1;
			
			//generate random values from 0-24
			Random rand = new Random(); //instance of random class
		    int upperbound = 25;
		    int random = rand.nextInt(upperbound); 
		    
		    int dummy = random;
					
			if (random%4==0) {
					
						line = line + word.getNoun(random);
									
				}	
			
			if (random%4==1) {
				
			
					line = line + word.getNoun(random);
					
				
			}
			if (random%4==2) {
				
					line = line + word.getAdverb(random);
			
			}	
			else {
				
					line = line + word.getAdjective(random);
					
				
			}
			
			line = line +" "+ labelList.get(i).getLabel() +" ";
			if(j%5==0)
				line= line + ".";
			random = dummy;
		}
		return line;
	}
	
}
