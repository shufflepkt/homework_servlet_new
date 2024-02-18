package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepository {
  private final CopyOnWriteArrayList<Post> postRepository = new CopyOnWriteArrayList<>();
  private final AtomicLong counter = new AtomicLong();

  public List<Post> all() {
    return new ArrayList<>(postRepository);
  }

  public Optional<Post> getById(long id) {
    if (postRepository.size() >= id) {
      return Optional.of(postRepository.get((int) (id - 1)));
    } else {
      throw new NotFoundException();
    }
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(counter.incrementAndGet());
      postRepository.add(post);
    } else if (post.getId() <= postRepository.size()) {
      postRepository.get((int) (post.getId() - 1)).setContent(post.getContent());
    } else {
      throw new NotFoundException();
    }
    return post;
  }

  public void removeById(long id) {
    if (postRepository.size() < id) {
      throw new NotFoundException();
    } else {
      postRepository.remove((int) (id - 1));
      counter.decrementAndGet();
      for (int i = (int) (id - 1); i < postRepository.size(); i++) {
        postRepository.get(i).setId(i + 1);
      }
    }
  }
}
