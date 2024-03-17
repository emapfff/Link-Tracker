package edu.java.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RelationLinkChatDto {
    Integer tg_chat_id;
    Integer link_id;
}
