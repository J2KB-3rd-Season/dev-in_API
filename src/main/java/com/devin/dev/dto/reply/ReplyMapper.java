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

        for (Reply origin : replies) {
            System.out.println("origin = " + origin);
            ReplyDto target = new ReplyDto();
            BeanUtils.copyProperties(origin, target);
            target.setName(origin.getUser().getName());

            for (ReplyImage next : origin.getImages()) {
                target.getImages().add(next.getPath());
            }

            replyDtos.add(target);
        }

        return replyDtos;
    }

}
