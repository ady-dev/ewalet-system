package com.ewallet_system.ewallet_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatusCode {
    // Generic error
    E_GENERIC_ERROR("89999", 500, "Internal server error", "Unmapped internal server error"),

    // Success response
    SUCCESS("00000", 200, "Success", "Success"),
    
    // Error response
    E_TIDAK_DAPAT_DIPROSES("80001", 400, "Permintaan tidak dapat diproses", "Kami akan melakukan pengecekan terlebih dahulu. Silakan kembali beberapa saat lagi"),
    E_TRANSAKSI_TIDAK_DITEMUKAN("80002", 404, "Transaksi tidak ditemukan", "Kami sedang mengecek ulang data. Silakan coba beberapa saat lagi, ya"),
    E_PERMINTAAN_GAGAL("80003", 400, "Permintaan gagal", "Gagal untuk membaca data"),
    ERROR_USER_NOT_FOUND("80004", 404, "User not found", "User not found"),

    // Error Business response
    B_PERMINTAAN_GAGAL("40001", 400, "Permintaan gagal", "Data belum bisa ditemukan"),
    B_DATA_GAGAL_DISIMPAN("40002", 400, "Data gagal disimpan", "Validasi data tidak berhasil"),
    B_TIDAK_ADA_PENGKINIAN_DATA("40003", 400, "Tidak ada pengkinian data", "Anda belum melakukan perubahan data sama sekali. Lakukan minimal 1 perubahan atau penambahan data"),
    ERROR_INVALID_AMOUNT("80004", 400, "Invalid amount", "Invalid amount"),
    ERROR_INSUFFICIENT_FUNDS("80005", 400, "Insufficient funds", "Insufficient funds"),
    ;



    private final String code;
    private final int httpCode;
    private final String title;
    private final String desc;

    public static ResponseStatusCode fromCode(String code) {
        if (code == null) return E_GENERIC_ERROR;
        for (ResponseStatusCode e : values()) {
            if (e.getCode().equals(code)) return e;
        }
        return E_GENERIC_ERROR;
    }
}
