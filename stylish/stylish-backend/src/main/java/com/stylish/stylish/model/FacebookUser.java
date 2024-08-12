package com.stylish.stylish.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FacebookUser {
    private String name;
    private String email;
    private FacebookPicture picture;

    @Getter
    @Setter
    public class FacebookPicture {
        private FacebookPictureData data;

        @Getter
        @Setter
        public class FacebookPictureData {
            private String url;
        }
    }
}
