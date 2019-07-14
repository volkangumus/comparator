package com.waes.comparator.controller;

import com.waes.comparator.controller.request.AspectEnum;
import com.waes.comparator.controller.request.EncodedDataRequest;
import com.waes.comparator.controller.response.DataResponse;
import com.waes.comparator.service.Base64DiffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by volkangumus on 14.07.2019
 */
@RestController
@RequestMapping("/v1/diff/{id}")
public class Base64DiffController {

    @Autowired
    private Base64DiffService diffService;

    @RequestMapping(value = "/left", method = RequestMethod.POST, produces = "application/json")
    private DataResponse saveLeft(@PathVariable Long id, @Valid @RequestBody EncodedDataRequest request) {

        diffService.save(id, request.getData(), AspectEnum.LEFT);

        return new DataResponse()
                .setMessage("Left side of entry registered");
    }

    @RequestMapping(value = "/right", method = RequestMethod.POST, produces = "application/json")
    private DataResponse saveRight(@PathVariable Long id, @Valid @RequestBody EncodedDataRequest request) {

        diffService.save(id, request.getData(), AspectEnum.RIGHT);

        return new DataResponse()
                .setMessage("Right side of entry registered");
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    private DataResponse getDiff(@PathVariable Long id) {
        String diffMessage = diffService.getDiff(id);

        return new DataResponse()
                .setMessage(diffMessage);
    }
}
