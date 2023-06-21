package hello.servlet.basic.request;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator().forEachRemaining(paramName -> System.out.printf("%s = %s%n", paramName, request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회]");
        final String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name);
        }

        Parameter parameter = new Parameter(username, age);
        Response<Parameter> responseResult = new ResponseBuilder<Parameter>().setSuccess(true).setMessage("provide parameter info").setResult(parameter).build();


        final Gson gson = new Gson();
        final String toJson = gson.toJson(responseResult);

        response.getWriter().write(toJson);
    }

    @RequiredArgsConstructor
    static class Parameter {
        private final String username;
        private final String age;
    }

    static class ResponseBuilder<T> {
        private boolean success;
        private String message;
        private T result;


        public ResponseBuilder<T> setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public ResponseBuilder<T> setMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder<T> setResult(T result) {
            this.result = result;
            return this;
        }

        public Response<T> build() {
            return new Response<>(success, message, result);
        }
    }

    @RequiredArgsConstructor
    static class Response<T> {
        private final boolean success;
        private final String message;
        private final T result;
    }

}
