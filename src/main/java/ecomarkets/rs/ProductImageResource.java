package ecomarkets.rs;

import ecomarkets.domain.ImageRepository;
import ecomarkets.domain.ProductImage;
import ecomarkets.domain.ProductImageBuilder;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/image")
public class ProductImageResource {

    @Inject
    private ImageRepository imageRepository;

    @POST
    @ResponseStatus(HttpStatus.SC_OK)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public void saveImage(ImageFormData imageFormData){
        ProductImage image = ProductImageBuilder.newInstance()
                .withBucket(imageRepository.getBucketName())
                .withFileName(imageFormData.fileName)
                .withMimeType(imageFormData.mimeType)
                .addTag("fileName", imageFormData.fileName).build();
        image.persist();
        imageRepository.save(imageFormData.file.toPath(), image);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getImage(@PathParam("id") Long id){
        return imageRepository.find(ProductImage.findById(id));
    }

    @Path("/{id}/presignedGetUrl")
    @GET
    public String createPresignedGetUrl(@PathParam("id") Long id){
        return imageRepository.createPresignedGetUrl(ProductImage.findById(id));
    }
}
