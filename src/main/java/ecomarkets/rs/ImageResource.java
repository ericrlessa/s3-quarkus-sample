package ecomarkets.rs;

import ecomarkets.domain.ImageDataRepository;
import ecomarkets.domain.Image;
import ecomarkets.domain.ImageBuilder;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/image")
public class ImageResource {

    @Inject
    private ImageDataRepository imageDataRepository;

    @POST
    @ResponseStatus(HttpStatus.SC_OK)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public void saveImage(ImageFormData imageFormData){
        Image image = ImageBuilder.newInstance()
                .withBucket(imageDataRepository.getBucketName())
                .withFileName(imageFormData.fileName)
                .withMimeType(imageFormData.mimeType)
                .addTag("fileName", imageFormData.fileName).build();
        image.persist();
        imageDataRepository.save(imageFormData.file.toPath(), image);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getImage(@PathParam("id") Long id){
        return imageDataRepository.find(Image.findById(id));
    }

    @Path("/{id}/presignedGetUrl")
    @GET
    public String createPresignedGetUrl(@PathParam("id") Long id){
        return imageDataRepository.createPresignedGetUrl(Image.findById(id));
    }
}
