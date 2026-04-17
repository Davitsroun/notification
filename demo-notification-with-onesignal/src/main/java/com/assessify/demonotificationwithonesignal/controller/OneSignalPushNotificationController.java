package com.assessify.demonotificationwithonesignal.controller;

import com.assessify.demonotificationwithonesignal.option.PushNotificationOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class OneSignalPushNotificationController {

    @PostMapping("/sendMessageToAllUsers")
    public void sendMessageToAllUsers(@RequestBody String message) throws JsonProcessingException {
        PushNotificationOptions.sendMessageToAllUsers(message);
    }

    @PostMapping({"/sendMessageToUser", "/sendMessageToUser/{userId}"})
    public void sendMessageToUser(@PathVariable(value = "userId", required = false) String userId,
                                  @RequestBody String message) throws JsonProcessingException {
        PushNotificationOptions.sendMessageToUser(message, userId);
    }

}
