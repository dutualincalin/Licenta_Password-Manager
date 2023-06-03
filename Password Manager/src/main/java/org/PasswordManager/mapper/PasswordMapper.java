package org.PasswordManager.mapper;


import org.PasswordManager.exceptions.InternalServerErrorException;
import org.PasswordManager.model.PasswordMetadata;
import org.PasswordManager.utility.Utils;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {
    PasswordMapper instance = Mappers.getMapper(PasswordMapper.class);

    default String imageToBase64String(BufferedImage image, String type) {
        String imageString;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        return imageString;
    }

    default JSONObject passwordMetadataToJSON(PasswordMetadata passwordMetadata) {
        JSONObject passParamsObject = new JSONObject()
            .put("website", passwordMetadata.getWebsite())
            .put("version", passwordMetadata.getVersion())
            .put("length", passwordMetadata.getLength())
            .put("date", Utils.DATE_FORMAT.format(passwordMetadata.getCreationDate()));

        if (passwordMetadata.getUsername() != null) {
            passParamsObject.put("username", passwordMetadata.getUsername());
        }

        return passParamsObject;
    }

    default String passwordMetadataListToJSON(ArrayList<PasswordMetadata> metaList) {
        if (metaList.isEmpty()) {
            return "";
        }
        JSONArray metaListArray = new JSONArray();
        metaList.forEach(metadata -> metaListArray.put(passwordMetadataToJSON(metadata)));
        return metaListArray.toString();
    }

    default PasswordMetadata jsonToPasswordMetadata(JSONObject metadataObject) {
        PasswordMetadata passwordMetadata;
        try {
            passwordMetadata = new PasswordMetadata(
                metadataObject.getString("website"),
                null,
                metadataObject.getInt("version"),
                metadataObject.getInt("length"),
                Utils.DATE_FORMAT.parse(metadataObject.getString("date"))
            );
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }

        if(metadataObject.has("username")) {
            passwordMetadata.setUsername(metadataObject.getString("username"));
        }

        return passwordMetadata;
    }

    default ArrayList<PasswordMetadata> jsonToPasswordMetadataList(String metaListString) {
        int i;
        JSONArray metaListArray = new JSONArray(metaListString);
        ArrayList<PasswordMetadata> metaList = new ArrayList<>();

        for(i = 0; i < metaListArray.length(); i++) {
            metaList.add(jsonToPasswordMetadata(metaListArray.getJSONObject(i)));
        }

        return metaList;
    }
}
