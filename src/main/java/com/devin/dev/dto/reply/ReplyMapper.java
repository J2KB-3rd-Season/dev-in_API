package com.devin.dev.dto.reply;

import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ReplyMapper {

    public static List<ReplyDto> toDtos(List<Reply> replies) {
        List<ReplyDto> replyDtos = new ArrayList<>();

        for (Reply reply : replies) {
            ReplyDto replyDto = replyToReplyDto(reply);

            replyDtos.add(replyDto);
        }

        return replyDtos;
    }

    public static ReplyDto replyToReplyDto(Reply reply) {
        ReplyDto replyDto = new ReplyDto();
        BeanUtils.copyProperties(reply, replyDto);
        replyDto.setName(reply.getUser().getName());

        for (ReplyImage next : reply.getImages()) {
            replyDto.getImages().add(next.getPath());
        }
        return replyDto;
    }

}
