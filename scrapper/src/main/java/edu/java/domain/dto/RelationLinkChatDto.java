package edu.java.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RelationLinkChatDto {
    Integer tgChatId;
    Integer linkId;
}
