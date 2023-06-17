package org.PasswordManager.mapper;


import org.PasswordManager.exceptions.InternalServerException;
import org.PasswordManager.model.PasswordConfiguration;
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


    /**
     ** Image conversion method
     ************************************************************************************/
    default String imageToBase64String(BufferedImage image, String type) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, type, bos);

            String imageString = Base64.getEncoder().encodeToString(bos.toByteArray());
            bos.close();

            return imageString;
        } catch (IOException e) {
            throw new InternalServerException();
        }
    }


    /**
     ** Password Configuration conversion methods
     ************************************************************************************/

    default JSONObject passwordConfigurationToJSON(PasswordConfiguration passwordConfiguration) {
        JSONObject passParamsObject = new JSONObject()
            .put("id", passwordConfiguration.getId())
            .put("website", passwordConfiguration.getWebsite())
            .put("version", passwordConfiguration.getVersion())
            .put("length", passwordConfiguration.getLength())
            .put("date", Utils.DATE_FORMAT.format(passwordConfiguration.getCreationDate()));

        if (passwordConfiguration.getUsername() != null) {
            passParamsObject.put("username", passwordConfiguration.getUsername());
        }

        return passParamsObject;
    }

    default String passwordConfiguratonListToJSON(ArrayList<PasswordConfiguration> metaList) {
        if (metaList.isEmpty()) {
            return "[]";
        }

        JSONArray metaListArray = new JSONArray();
        metaList.forEach(metadata -> metaListArray.put(passwordConfigurationToJSON(metadata)));
        return metaListArray.toString();
    }


    /**
     ** JSON conversion methods
     ************************************************************************************/

    default PasswordConfiguration jsonToPasswordConfiguration(JSONObject metadataObject) {
        PasswordConfiguration passwordConfiguration;
        try {
            passwordConfiguration = new PasswordConfiguration(
                metadataObject.getString("website"),
                null,
                metadataObject.getInt("version"),
                metadataObject.getInt("length"),
                Utils.DATE_FORMAT.parse(metadataObject.getString("date"))
            );

            passwordConfiguration.setId(metadataObject.getString("id"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InternalServerException();
        }

        if(metadataObject.has("username")) {
            passwordConfiguration.setUsername(metadataObject.getString("username"));
        }

        return passwordConfiguration;
    }

    default ArrayList<PasswordConfiguration> jsonToPasswordConfigurationList(String metaListString) {
        int i;
        JSONArray metaListArray = new JSONArray(metaListString);
        ArrayList<PasswordConfiguration> metaList = new ArrayList<>();

        for(i = 0; i < metaListArray.length(); i++) {
            metaList.add(jsonToPasswordConfiguration(metaListArray.getJSONObject(i)));
        }

        return metaList;
    }
}
