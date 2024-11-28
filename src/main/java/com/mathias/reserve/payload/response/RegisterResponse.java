package com.mathias.reserve.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String responseCode;
    private String responseMessage;
}
