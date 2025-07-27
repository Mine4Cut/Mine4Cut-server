package io.github.Mine4Cut.Mine4Cut_server.service.auth.dto;

import java.util.Date;

public record ParsedJwtInfo(
    String username,
    String role,
    Date issuedAt,
    Date expiration
) {

}
