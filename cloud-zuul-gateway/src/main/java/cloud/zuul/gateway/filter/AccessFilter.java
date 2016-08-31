package cloud.zuul.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
@Component
public class AccessFilter extends ZuulFilter{

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
    
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        System.out.println("requestURI"+requestURI);
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL"+requestURL.toString());
        
        Object accessToken = request.getParameter("accessToken");
        if(accessToken == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        return null;
    }
}
