/**
 * ==========================================================================
 * __      __ _ __   ___  *    WellProdSim                                  *
 * \ \ /\ / /| '_ \ / __| *    @version 1.0                                 *
 *  \ V  V / | |_) |\__ \ *    @since 2023                                  *
 *   \_/\_/  | .__/ |___/ *                                                 *
 *           | |          *    @author Jairo Serrano                        *
 *           |_|          *    @author Enrique Gonzalez                     *
 * ==========================================================================
 * Social Simulator used to estimate productivity and well-being of peasant *
 * families. It is event oriented, high concurrency, heterogeneous time     *
 * management and emotional reasoning BDI.                                  *
 * ==========================================================================
 */
package org.wpsim.PeasantFamily.Goals.L4SkillsResources;

import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.WellProdSim.Base.wpsGoalBDI;
import org.wpsim.PeasantFamily.Tasks.L4SkillsResources.AlternativeWorkTask;
import org.wpsim.WellProdSim.wpsStart;
import rational.RationalRole;
import rational.mapping.Believes;
import rational.mapping.Plan;

/**
 *
 * @author jairo
 */
public class AlternativeWorkGoal extends wpsGoalBDI {

    /**
     *
     * @return
     */
    public static AlternativeWorkGoal buildGoal() {
        AlternativeWorkTask alternativeWorkTask = new AlternativeWorkTask();
        Plan alternativeWorkPlan = new Plan();
        alternativeWorkPlan.addTask(alternativeWorkTask);
        RationalRole alternativeWorkRole = new RationalRole(
                "AlternativeWorkTask",
                alternativeWorkPlan);
        return new AlternativeWorkGoal(
                wpsStart.getPlanID(),
                alternativeWorkRole,
                "AlternativeWorkTask",
                GoalBDITypes.REQUIREMENT);
    }

    /**
     *
     * @param id
     * @param role
     * @param description
     * @param type
     */
    public AlternativeWorkGoal(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double detectGoal(Believes parameters) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;

        if (this.isAlreadyExecutedToday(believes)) {
            return 0;
        }

        if (believes.getPeasantProfile().getMoney() <= wpsStart.config.getIntProperty("pfagent.alternativethreshold") || wpsStart.config.getBooleanProperty("pfagent.earlyalternative")) {
            return 1;
        }

        return 0;
    }

    /**
     *
     * @param parameters
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluatePlausibility(Believes parameters) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        if (believes.haveTimeAvailable(TimeConsumedBy.AlternativeWorkTask)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param stateBDI
     * @return
     * @throws KernellAgentEventExceptionBESA
     */
    @Override
    public double evaluateContribution(StateBDI stateBDI) throws KernellAgentEventExceptionBESA {
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) stateBDI.getBelieves();
        double partialContribution;
        if (believes.getPeasantProfile().getMoney() < wpsStart.config.getIntProperty("pfagent.alternativethreshold")) {
            partialContribution = 0.9;
        }else{
            partialContribution = 0.6;
        }
        return evaluateEmotionalContribution(stateBDI, partialContribution);
    }

}