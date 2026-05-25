package com.restaurent.RMS.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrivilegeStatus {
    READ,
    WRITE,
    MAINTAIN,
    NONE
}
