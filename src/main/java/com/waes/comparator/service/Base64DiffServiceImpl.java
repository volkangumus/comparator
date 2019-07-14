package com.waes.comparator.service;

import com.waes.comparator.controller.request.AspectEnum;
import com.waes.comparator.entity.Base64Entry;
import com.waes.comparator.repository.Base64DiffRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by volkangumus on 14.07.2019
 */
@Service
public class Base64DiffServiceImpl implements Base64DiffService {

    @Autowired
    private Base64DiffRepository diffRepository;

    @Override
    public void save(Long id, String data, AspectEnum aspect) {
        Base64Entry entry = diffRepository.findById(id).orElse(null);

        if(Objects.isNull(entry)) {
            entry = new Base64Entry()
                    .setId(id);
        }

        switch (aspect) {
            case LEFT:
                entry.setLeft(data);
                break;

            case RIGHT:
                entry.setRight(data);
                break;

            default:
                throw new IllegalArgumentException("unsupported aspect: " + aspect.value());
        }

        diffRepository.save(entry);
    }

    @Override
    public String getDiff(Long id) {
        Base64Entry entry = diffRepository.findById(id).orElse(null);
        if(Objects.isNull(entry)) {
            return "No entry found for the id: " + id;
        }

        if(StringUtils.isBlank(entry.getLeft()) || StringUtils.isBlank(entry.getRight())) {
            return "Left side or Right side of the entry is blank";
        }

        byte[] bytesLeft = entry.getLeft().getBytes();
        byte[] bytesRight = entry.getRight().getBytes();

        StringBuilder offsetBuilder = new StringBuilder();
        if(Arrays.equals(bytesLeft, bytesRight)) {
            return "Left and Right base64 datas are equal";
        } else if(bytesLeft.length != bytesRight.length) {
            return "Left and Right base64 datas do not have same size";
        } else {
            /*
             now left and right sides are not equal, but they are in same length
             So we have to determine theirs getDiff in terms of offset
             */
            byte xor = -1;
            for (int i=0; i<bytesLeft.length; i++) {

                // xor values, to see getDiff
                xor = ((byte) (bytesLeft[i] ^ bytesRight[i]));

                // this means bytes in this index are different
                if(xor != 0) {

                    // adding all different indexes
                    offsetBuilder.append(" ").append(i);
                }
            }
        }

        return "Left and Right base64 datas are in same size and here is offset diff:" + offsetBuilder.toString();
    }
}
