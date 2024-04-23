package jp.co.illmatics.apps.shopping.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import jp.co.illmatics.apps.shopping.session.AdminAccount;

@Aspect
@Component
public class CheckLoginAspect {
	@Autowired
	AdminAccount account;
	
//	@Around("execution(* jp.co.illmatics.apps.shopping.controller.account.*Controller.*(..))")
	@Around("execution(* jp.co.illmatics.apps.shopping.controller..Admin*Controller.*(..))")
	public Object checkLogin(ProceedingJoinPoint jp) throws Throwable {
		Signature sig = jp.getSignature();
		System.out.println("現在のコントローラー" + sig.getDeclaringType().getSimpleName() + "#" + sig.getName());
		
		// ログイン関連コントローラー
		// ログインしていなくても遷移可能
		if(sig.getDeclaringType().getSimpleName().equals("LoginController")) {
			return jp.proceed();
		}
		
		// ログインしていない場合
		// 管理者ログインページ遷移
		if(ObjectUtils.isEmpty(account.getName()) || ObjectUtils.isEmpty(account.getEmail())) {
			return "redirect:/admin/login";
		}
		
		return jp.proceed();
	}
}
