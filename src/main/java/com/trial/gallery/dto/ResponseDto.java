package com.trial.gallery.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseDto<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;

    private Integer code;

    private String message;

    private T payLoad = null;


    public ResponseDto(String success, int i, String profilePhotoUpdatedSuccessfully, String photoUploaded) {
    }
}

