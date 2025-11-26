package org.example.smartshop.utils;

import lombok.*;
import org.example.smartshop.enums.UserRole;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionUser implements Serializable {
    private Long id;
    private String username;
    private UserRole role;
    private Long clientId;
}
