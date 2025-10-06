package com.webit.webit.model;

import com.webit.webit.util.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
public class User extends AbstractClass {
    @Id
    private UUID userId;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private Role role;

    private String avatar;

    private String resume;

    private String companyName;

    private String companyDescription;

    private String companyLogo;

}
