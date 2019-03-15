/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.config.datasource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Randy
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"prosia.app.repo", "prosia.imi.repo"})
@EntityScan(basePackages = {"prosia.app.model", "prosia.imi.model"})
public class MySqlDataSource {

}
