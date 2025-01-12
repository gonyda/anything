package com.bbsk.anything.naver.finance.controller;

import com.bbsk.anything.naver.finance.sevice.NaverFinanceService;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor
public class NaverFinanceController {

    private final NaverFinanceService naverFinanceService;

    @PostMapping("/{ticker}/{company}")
    public ResponseEntity<String> addCompanyPerformance(@PathVariable String ticker, @PathVariable String company,
                                                        @AuthenticationPrincipal User user) {
        return naverFinanceService.processAndSaveCompanyPerformance(ticker, company, user) ?
                ResponseEntity.ok("회사 실적이 성공적으로 처리되었습니다.") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회사 실적 처리 중 에러가 발생하였습니다.");
    }
}
