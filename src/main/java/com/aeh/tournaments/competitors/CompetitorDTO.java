package com.aeh.tournaments.competitors;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitorDTO {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @Min(4)
    @Max(100)
    private int age;
    private String category;
    @NotNull
    private String club;
    private boolean skippedLast;

    public Competitor toEntity() {
        Competitor competitor = new Competitor();
        competitor.setId(id);
        competitor.setName(name);
        competitor.setSurname(surname);
        competitor.setAge(age);
        competitor.setCategory(category);
        competitor.setClub(club);
        competitor.setSkippedLast(skippedLast);
        return competitor;
    }

    public static CompetitorDTO toDto(Competitor competitor) {
        if (competitor == null) {
            return new CompetitorDTO();
        }
        CompetitorDTO competitorDTO = new CompetitorDTO();
        competitorDTO.setId(competitor.getId());
        competitorDTO.setName(competitor.getName());
        competitorDTO.setSurname(competitor.getSurname());
        competitorDTO.setAge(competitor.getAge());
        competitorDTO.setCategory(competitor.getCategory());
        competitorDTO.setClub(competitor.getClub());
        competitorDTO.setSkippedLast(competitor.isSkippedLast());
        return competitorDTO;
    }
}
