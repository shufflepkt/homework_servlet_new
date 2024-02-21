package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContextExtensionsKt;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";
  private static final String PATH = "/api/posts";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.netology");
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(METHOD_GET) && path.equals(PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals(METHOD_GET) && path.matches(PATH + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(METHOD_POST) && path.equals(PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(METHOD_DELETE) && path.matches(PATH + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

