package org.zerock.memberboard.service;

import org.zerock.memberboard.dto.MovieDTO;
import org.zerock.memberboard.dto.MovieImageDTO;
import org.zerock.memberboard.dto.PageRequestDTO;
import org.zerock.memberboard.dto.PageResultDTO;
import org.zerock.memberboard.entity.Movie;
import org.zerock.memberboard.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieDTO movieDTO) ;

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){// dtoToEntity는 map타입으로 movie객체 + movieImg객체의 리스트 처리

        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder().
                mno(movieDTO.getMno()).
                title(movieDTO.getTitle()).build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0) {
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO ->
            {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie).build();
                return movieImage;
            }).collect(Collectors.toList());  //객체를 새로운 리스트로 만드는 방법
            entityMap.put("imgList", movieImageList);

        }
        return entityMap;


    }

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO  pageRequestDTO);// 목록 처리하기~

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt) {
        //entitiesToDTO -> movie엔티티 + 영화 이미지 리스트 + 평점 평균 + 리뷰 개수 를 파라미터를 받는다.

        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {

            return MovieImageDTO.builder().imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .uuid(movieImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt.intValue());

        return movieDTO;
    }

   // default Map<String, Object[]> dtoToEntity(MovieDTO movieDTO){}

}
