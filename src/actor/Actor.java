package actor;

import java.util.List;

public class Actor {
    private String name;
    private String careerDescr;
    private List<String> filmography;
    private Awards[] awards;

    public String getName() {        return name; }

    public String getCareerDescr() {    return careerDescr;    }

    public List<String> getFilmography() {  return filmography;    }

    public Awards[] getAwards() {   return awards;  }


    public void setName(String name) {        this.name = name;    }

    public void setCareerDescr(String careerDescr) {        this.careerDescr = careerDescr;    }

    public void setFilmography(List<String> filmography) {        this.filmography = filmography;    }

    public void setAwards(Awards[] awards) {        this.awards = awards;    }
}
