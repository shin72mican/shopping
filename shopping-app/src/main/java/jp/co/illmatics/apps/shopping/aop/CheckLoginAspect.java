package jp.co.illmatics.apps.shopping.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import jp.co.illmatics.apps.shopping.session.AdminAccount;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Aspect
@Component
public class CheckLoginAspect {
	@Autowired
	AdminAccount adminAccount;
	
	@Autowired
	UserAccount userAccount;
	
//	@Around("execution(* jp.co.illmatics.apps.shopping.controller.account.*Controller.*(..))")
	
	// 管理者ログインAOP
	@Around("execution(* jp.co.illmatics.apps.shopping.controller..Admin*Controller.*(..))")
	public Object checkAdminLogin(ProceedingJoinPoint jp) throws Throwable {
		Signature sig = jp.getSignature();
		System.out.println("現在のコントローラー#アクション" + sig.getDeclaringType().getSimpleName() + "#" + sig.getName());
		
		// ログインしていない場合
		// 管理者ログインページ遷移
		if (ObjectUtils.isEmpty(adminAccount.getName()) || ObjectUtils.isEmpty(adminAccount.getEmail())) {
			return "redirect:/admin/login";
		}
		
		// ログイン関連コントローラー
		// ログインしていなくても遷移可能
		if (sig.getDeclaringType().getSimpleName().equals("LoginController")) {
			return jp.proceed();
		}
		
		if (sig.getDeclaringType().getSimpleName().equals("AdminController")) {
			if (adminAccount.getAuthority().equals("owner")) {
				return jp.proceed();
			} else if (adminAccount.getAuthority().equals("general")) {
				return "redirect:/errors/403";
			} else {
				System.err.println("権限が異常なアカウントです");
				return "redirect:/errors/403";
			}
		}
		
		return jp.proceed();
	}
	
	// 顧客ログインAOP
	@Around("execution(* jp.co.illmatics.apps.shopping.controller..User*Controller.*(..))")
	public Object checkUserLogin(ProceedingJoinPoint jp) throws Throwable {
		Signature sig = jp.getSignature();
		
		// ログインしていない場合
		// 顧客ログインページ遷移
		if (ObjectUtils.isEmpty(userAccount.getName()) || ObjectUtils.isEmpty(userAccount.getEmail())) {
			return "redirect:/login";
		}
		
		return jp.proceed();
	}
}
