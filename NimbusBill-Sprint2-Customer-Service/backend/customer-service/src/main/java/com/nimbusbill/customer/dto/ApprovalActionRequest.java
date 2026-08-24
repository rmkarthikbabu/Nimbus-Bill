package com.nimbusbill.customer.dto;import jakarta.validation.constraints.Size;public record ApprovalActionRequest(@Size(max=500) String comments){}
