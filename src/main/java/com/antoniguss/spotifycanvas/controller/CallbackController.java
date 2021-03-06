package com.antoniguss.spotifycanvas.controller;

import com.antoniguss.spotifycanvas.constant.ApiPath;
import com.antoniguss.spotifycanvas.dto.CurrentPlayingDto;
import com.antoniguss.spotifycanvas.exception.NoTrackPlayingException;
import com.antoniguss.spotifycanvas.service.AccessTokenService;
import com.antoniguss.spotifycanvas.service.CurrentPlayingService;
import com.antoniguss.spotifycanvas.service.ProfileDetailService;
import com.antoniguss.spotifycanvas.service.SpotifyUrlService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@SuppressWarnings("ALL")
@Controller
@AllArgsConstructor
@RestController
public class CallbackController {

    private final SpotifyUrlService spotifyUrlService;
    private final AccessTokenService accessTokenService;
    private final ProfileDetailService userDetails;
    private final CurrentPlayingService currentPlaying;



//    @GetMapping(value = ApiPath.CALLBACK)
    @RequestMapping(value = ApiPath.CALLBACK)
    public CurrentPlayingDto handleCallback
    (
            @RequestParam(value = "code", required = false) final String code,
            @RequestParam(value = "error", required = false) final String error,
            final Model model,
            final HttpSession session) {

//        if (error != null) {
//            model.addAttribute("url", spotifyUrlService.getAuthorizationURL());
//            return Template.CALLBACK_FAILURE;
//        }

        session.setAttribute("code", code);
        String token = accessTokenService.getToken(code);

        session.setAttribute("accessToken", token);
        model.addAttribute("accessToken", token);
        model.addAttribute("username", userDetails.getUsername(token));

        try {
            model.addAttribute("currentPlaying", currentPlaying.getCurrentPlaying(token));
        } catch (NoTrackPlayingException exception) {
            model.addAttribute("display", 0);
        }
        
//        return Template.CALLBACK_SUCCESS;

        return currentPlaying.getCurrentPlaying(token);
    }

}
