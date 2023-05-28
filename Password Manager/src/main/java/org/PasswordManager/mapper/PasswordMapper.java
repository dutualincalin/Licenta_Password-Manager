package org.PasswordManager.mapper;


import org.PasswordManager.model.PasswordMetadata;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {
    PasswordMapper instance = Mappers.getMapper(PasswordMapper.class);

    default String imageToBase64String(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    default JSONObject passwordMetadataToJSON(PasswordMetadata passwordMetadata) {
        JSONObject passParamsObject = new JSONObject()
            .put("website", passwordMetadata.getWebsite());

        if (passwordMetadata.getUsername() != null) {
            passParamsObject.put("username", passwordMetadata.getUsername());
        }

        if (passwordMetadata.getVersion() != 0) {
            passParamsObject.put("version", passwordMetadata.getVersion());
        }

        if (passwordMetadata.getLength() != 0) {
            passParamsObject.put("length", passwordMetadata.getLength());
        }

        return passParamsObject;
    }

    default String passwordMetadataListToJSON(ArrayList<PasswordMetadata> metaList) {
        JSONArray metaListArray = new JSONArray();
        metaList.forEach(metadata -> metaListArray.put(passwordMetadataToJSON(metadata)));
        return metaList.toString();
    }

    default PasswordMetadata jsonToPasswordMetadata(JSONObject metadataObject) {
        PasswordMetadata passwordMetadata = new PasswordMetadata(
            metadataObject.getString("website")
        );

        if(metadataObject.has("username")) {
            passwordMetadata.setUsername(metadataObject.getString("username"));
        }

        if(metadataObject.has("version")) {
            passwordMetadata.setVersion(metadataObject.getInt("version"));
        }

        if(metadataObject.has("length")) {
            passwordMetadata.setLength(metadataObject.getInt("length"));
        }

        return passwordMetadata;
    }

    default ArrayList<PasswordMetadata> jsonToPasswordMetadataList(JSONArray metaListArray) {
        int i;
        ArrayList<PasswordMetadata> metaList = new ArrayList<>();

        for(i = 0; i < metaListArray.length(); i++) {
            metaList.add(jsonToPasswordMetadata(metaListArray.getJSONObject(i)));
        }

        return metaList;
    }
}
