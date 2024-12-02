package com.mathias.reserve.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TerminalDto {
    private String terminalName;
    private Long stateId; // ID of the associated state
}
