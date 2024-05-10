package com.aeh.tournaments.competitors;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitorDTO {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @Min(4)
    @Max(100)
    private int age;
    private String gender;
    private String competition;
    @NotNull
    private String club;
    private int advancement;
    private int weight;
    private boolean skippedLast;

    public Competitor toEntity() {
        Competitor competitor = new Competitor();
        competitor.setId(id);
        competitor.setName(name);
        competitor.setSurname(surname);
        competitor.setAge(age);
        competitor.setGender(gender);
        competitor.setCompetition(competition);
        competitor.setClub(club);
        competitor.setAdvancement(advancement);
        competitor.setWeight(weight);
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
        competitorDTO.setSurname(competitorDTO.getSurname());
        competitorDTO.setAge(competitor.getAge());
        competitorDTO.setGender(competitor.getGender());
        competitorDTO.setCompetition(competitor.getCompetition());
        competitorDTO.setClub(competitor.getClub());
        competitorDTO.setAdvancement(competitor.getAdvancement());
        competitorDTO.setWeight(competitor.getWeight());
        competitorDTO.setSkippedLast(competitor.isSkippedLast());
        return competitorDTO;
    }
}
